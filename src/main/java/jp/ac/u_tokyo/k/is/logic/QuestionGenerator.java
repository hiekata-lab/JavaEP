package jp.ac.u_tokyo.k.is.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.ac.u_tokyo.k.is.data.Question;

import org.codehaus.jackson.map.ObjectMapper;

public class QuestionGenerator {
	public static Map<Integer, Question> execute(String dstPath, String javaPolicyPath) throws IOException {
		Map<Integer, Question> map = new HashMap<Integer, Question>();

		File dir = new File(dstPath);
		String[] filenames = dir.list();

		for (String filename : filenames) {
			if (filename.endsWith(".java")) {
				String questionName = filename.replace(".java", "");

				int id = Integer.parseInt(questionName.split("_")[1]);
				System.out.println("id=" + id);

				// HTML
				String htmlPath = dstPath + "/" + questionName + ".html";
				String htmlContents = readHtml(htmlPath);
				//System.out.println("html=" + htmlContents);

				// 実行時引数
				String argsPath = dstPath + "/" + questionName + ".cmd_options";
				String cmdOptions = readArgs(argsPath);
				System.out.println("args=" + cmdOptions);

				// Javaファイル
				String javaPath = dstPath + "/" + questionName + ".java";
				String source = readJava(javaPath, id);
				// System.out.println("java=" + source);

				// 解答
				String answer = getResult(cmdOptions, dstPath, id, source, javaPolicyPath);
				System.out.println("answer=" + answer);

				// 難易度
				String scorePath = dstPath + "/" + questionName + ".attr";
				int score = readAttr(scorePath);
				System.out.println("score=" + score);

				Question question = new Question();
				question.setDifficulty(score);
				question.setContent(htmlContents);
				question.setArgs(cmdOptions);
				question.setAnswers(answer);
				question.setSource(source);

				map.put(id, question);

				System.out.println("--------------------------------");
			}
		}

		return map;
	}

	public static String readFile(String path) throws IOException {
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sb = new StringBuilder();

		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		String contents = sb.toString();
		br.close();

		return contents.trim();
	}

	public static String readHtml(String path) throws IOException {
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sb = new StringBuilder();
		
		while ((line = br.readLine()) != null) {
			sb.append(line + "\r\n");
		}
		
		br.close();
		
		// String contents = readFile(path);
		String contents = sb.toString().trim();
		
		// Body部の切り出し
		contents = contents.split("<body>")[1];
		contents = contents.split("</body>")[0];

		return contents;
	}

	public static String readArgs(String path) throws IOException {
		String contents = readFile(path);
		String[] elms = contents.split("ARGS:");
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(elms).replaceFirst("\\[\"\",", "[");
	}


	public static String readJava(String path, int id) throws IOException {
		//　Javaファイルの読み込みだけreadFile()メソッドを使わない
		File file = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		StringBuilder sb = new StringBuilder();

		while ((line = br.readLine())!= null) {
			if (line.contains("question_")) {
				line = "public class Question" + id + " { "; // クラス名の書き換え
			}
			sb.append(line + "\r\n");//コメントアウトのエラー対策
		}

		String contents = sb.toString();
		br.close();

		return contents;
	}

	// ANSWERの取得
	public static String getResult(String cmdOptions, String dstPath, int id, String javaContents, String javaPolicyPath) throws IOException {
		String[] options = Util.parseJsonToArray(cmdOptions);

		Marker marker = new Marker(dstPath);
		String[] results = marker.getResults("Question" + id, javaContents, options, javaPolicyPath);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(results);
	}

	public static int readAttr(String path) throws IOException {
		String contents = readFile(path);
		contents = contents.split(",")[1];
		return Integer.parseInt(contents.trim());
	}
}

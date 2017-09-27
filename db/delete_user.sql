use javaep;
set @delete_user = 'targetuser';
delete from authorities where username = @delete_user;
delete from qandas where username = @delete_user;
delete from students_status where username = @delete_user;
delete from students_status_exam where username = @delete_user;
delete from students_status_exam_log where username = @delete_user’;
delete from students_status_log where username = @delete_user;
delete from users where username = @delete_user;


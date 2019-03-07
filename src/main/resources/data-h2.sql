insert into congregation (name) values ('신길동부');
insert into member (name, pw, role, user_id, congregation_id) values ('admin', '000000', 'ADMIN', 'admin', 1);
insert into member (name, pw, role, user_id, congregation_id) values ('한상호', '000000', 'ADMIN', '한상호', 1);
insert into member (name, pw, role, user_id, congregation_id) values ('오민경', '000000', 'BASIC', '오민경', 1);
insert into member (name, pw, role, user_id, congregation_id) values ('이대현', '000000', 'MANAGER', '이대현', 1);
insert into member (name, pw, role, user_id, congregation_id)values ('백신애', '000000', 'BASIC', '백신애', 1);
insert into exhibition (guide, name, time, limitation, congregation_id) values ('한상호', '공군회관', '일요일 오전', 10, 1);

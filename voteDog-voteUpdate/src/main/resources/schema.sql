use voteDog;
drop table if exists voteDog.dogs;
create table voteDog.dogs
(
    id          bigint auto_increment
        primary key,
    count       int          not null,
    description varchar(200) not null,
    image       text         not null,
    name        varchar(20)  not null
);

insert into dogs (id, count, description, image, name)
values (1, 50, '내 갱얼쥐', 'https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/holapet/20210525081724428qquq.jpg', '코코');
insert into dogs (id, count, description, image, name)
values (2, 30, '기여운 강아지', 'https://img2.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/holapet/20210525081725668haup.png', '초코');
insert into dogs (id, count, description, image, name)
values (3, 30, '백설기아니고 강아지입니다', 'https://img3.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/holapet/20210525081726892iyzj.jpg', '시로');
insert into dogs (id, count, description, image, name)
values (4, 100, '장인 장모 치와와', 'https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/202105/25/holapet/20210525081729350fczm.jpg', '설탕이');
insert into dogs (id, count, description, image, name)
values (5, 70, '솜사탕아니고 강아지', 'https://images.mypetlife.co.kr/content/uploads/2022/06/28123435/KakaoTalk_20220602_090443576-1-1-edited-scaled.jpg', '봄');

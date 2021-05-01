insert into user (id, first, last, middle, age) values ( 'c_1', 'John', 'Doe', 'Maven' , 31);
insert into user (id, first, last, middle, age) values ( 'c_2', 'Jane', 'Dove', 'Gradle', 41);

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_1', 'The tattered work gloves speak of the many hours of hard labor he endured throughout his life', now(),
        '#work#labour', 'Office', 'uri://sample1', 20, 'LITERATURE', 'c_1' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_2', 'Dolores wouldn''t have eaten the meal if she had known what it actually was.', now(),
         '#food', 'Office', 'uri://sample1', 7686, 'MUSIC', 'c_1' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_3', 'It caught him off guard that space smelled of seared steak.', now(),
         '#food#stale#caughtinact', 'Office', 'uri://sample1', 1234, 'LITERATURE', 'c_1' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_4', '25 years later, she still regretted that specific moment.', now(),
         '#ages', 'Office', 'uri://sample1', 679, 'MOVIE', 'c_1' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_5', 'She had a habit of taking showers in lemonade.', now(),
         '#bath', 'Office', 'uri://sample1', 23, 'LITERATURE', 'c_1' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_6', 'She is never happy until she finds something to be unhappy about; then, she is overjoyed.', now(),
         '#feelings', 'Office', 'uri://sample1', 4569877, 'MOVIE', 'c_2' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_7', 'They throw cabbage that turns your brain into emotional baggage.', now(),
         '#food', 'Office', 'uri://sample1', 1000, 'MUSIC', 'c_2' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_8', 'There''s a message for you if you look up.', now(),
         '#message', 'Office', 'uri://sample1', 20, 'LITERATURE', 'c_2' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_9', 'Please wait outside of the house.', now(),
         '#house#labour', 'Office', 'uri://sample1', 40, 'MUSIC', 'c_2' );

insert into post (id, body, created_at, hash_tags, image_description, image_url, like_count, tag, user_id)
values ( 'p_10', 'Honestly, I didn''t care much for the first season, so I didn''t bother with the second.', now(),
         '#honest#season#time', 'Office', 'uri://sample1', 20000, 'MOVIE', 'c_2' );






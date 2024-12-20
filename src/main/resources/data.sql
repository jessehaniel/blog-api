INSERT INTO tb_users (created_at, updated_at, deleted, username, password, email, display_name, dob, account_expired, account_locked, credentials_expired, role)
VALUES
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'user1', '$2a$12$mE56o5Q.763eUYQDtAOt0eW2domkBoPbgkuydJExwRCwC83hmeIZC', 'user1@email.com', 'User One', '1985-01-01', FALSE, FALSE, FALSE, 'COMMON_USER'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'user2', '$2a$12$49OeKx9FafiAOWKEFa.SkuuSr5EPdLk7PUlIQEDkehZ4/wSOKN8.m', 'user2@email.com', 'User Two', NULL, FALSE, FALSE, FALSE, 'COMMON_USER'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'admin1', '$2a$12$xVfomrQE93OefjoG1eUunOJBxkW0MHasoSEsHkoxOu8EqJWy1GMdO', 'admin1@email.com', 'Admin One', '1978-03-15', FALSE, FALSE, FALSE, 'ADMIN'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'moderator', '$2a$12$8vfTOb2JRxCv5zwDTJ6U8eJECXZGXeBMXMtq64EccbrC3vZFdse62', 'moderator@email.com', 'Moderator User', '1992-06-30', FALSE, FALSE, FALSE, 'ADMIN'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'guest', '$2a$12$uCoFnfowajejJVAsjQY1weIcVKAiru7SxMGmVeJriP/tkqIFGZcfy', 'guest@email.com', 'Guest User', '1999-12-01', TRUE, FALSE, FALSE, 'GUEST'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'user3', '$2a$12$28i.SOY71kUHgl6kKuL50eKZpVOm4CqEpMErkYA57hxp4hdxCUerO', 'user3@email.com', 'User Three', NULL, FALSE, FALSE, TRUE, 'COMMON_USER'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'support', '$2a$12$RaX2.H.qde3xTpWes8yp0ecwy.ybQD/8mcjcRkcEyFDAjMjgGEPaS', 'support@email.com', 'Support Agent', '1995-10-12', FALSE, FALSE, FALSE, 'COMMON_USER'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'devops', '$2a$12$3HUQSpNkKlJYxuQtwJPty.M./.ETrt/9lZj7K/eMRgda/9hSG04ua', 'devops@email.com', 'DevOps Engineer', NULL, FALSE, FALSE, FALSE, 'GUEST'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'tester', '$2a$12$SHVzKDMvKzRVVJB2UhtoTuFxTkETW8kClRDwmak1ZEYh36qsVj.Fe', 'tester@email.com', 'QA Tester', '1991-08-22', FALSE, FALSE, TRUE, 'GUEST'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'user10', '$2a$12$OdguNYcVyaRPWkL4m2myNO1msy.TbfKJ8apjdqwYm3ezmd8Jn9p3i', 'user10@email.com', 'User Ten', '2000-05-15', FALSE, FALSE, FALSE, 'GUEST');

INSERT INTO tb_posts (created_at, updated_at, deleted, uuid, title, content, author_id, author_username)
VALUES
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '4aaab88e-3c54-4f88-b1e1-1b2a14d2e4a1', 'First Post', 'This is the content of the first post', 1, 'user1'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '566db3e3-a432-4871-93aa-9469b9771d8f', 'Second Post', 'Content for the second post', 2, 'user2'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '888b644e-b625-4c71-b98c-4ea6e4dd2e7f', 'Admin Post 1', 'Post created by an admin', 3, 'admin1'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'fa89b6e1-e715-4822-9891-4bdf6cb9c121', 'Moderator Update', 'Moderation team announcement', 4, 'moderator'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'ae098b3d-f560-47cb-a395-9d44a117cc6b', 'Guest Content', 'Guest user contribution', 5, 'guest'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '082ba3e7-d451-4ae1-8da3-d1ff44b79ca3', 'User Post', 'Random user-generated content', 6, 'user3'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '198bc446-cd9e-4cd4-b1ed-ecc07b04a8a1', 'DevOps Tips', 'Sharing DevOps tips and tricks', 8, 'devops'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '378cd3b2-05eb-48a7-b017-ace95767a4e7', 'Tester Insights', 'Quality assurance insights', 9, 'tester'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '468ed6e3-51ac-4aa7-8764-fbb04ae3cdef', 'Support News', 'Updates from the support team', 7, 'support'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '58cab3e3-d4a6-4b11-97e5-ccb22b1197ee', 'User Post 10', 'Additional random content', 10, 'user10');


INSERT INTO tb_comments (created_at, updated_at, deleted, uuid, content, blog_post_id, author_id, author_username, blog_post_uuid)
VALUES
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '1423a78d-dc4e-40fd-92f1-bc5a58e594a7', 'Nice post!', 1, 2, 'user2', '4aaab88e-3c54-4f88-b1e1-1b2a14d2e4a1'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '25a3e7d6-fcd4-4cb2-938d-efa8b06e3a19', 'Thanks for sharing this.', 2, 3, 'admin1', '566db3e3-a432-4871-93aa-9469b9771d8f'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '3a24b7c4-4cd3-46cb-bef8-ca927114c1d5', 'Great content as usual.', 3, 4, 'moderator', '888b644e-b625-4c71-b98c-4ea6e4dd2e7f'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '47d3b9e5-5d52-40fc-8e29-db57a19406cf', 'I agree with this!', 1, 5, 'guest', '4aaab88e-3c54-4f88-b1e1-1b2a14d2e4a1'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '507a65b8-d13a-4cc3-be94-0af17cb9e1ab', 'Helpful tips, thanks!', 7, 8, 'devops', '198bc446-cd9e-4cd4-b1ed-ecc07b04a8a1'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '65ac3b8a-cc42-4cd4-a987-df4bd6501c7b', 'Interesting perspective.', 3, 6, 'user3', '888b644e-b625-4c71-b98c-4ea6e4dd2e7f'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '7486ab3e-4cd3-4762-b910-bcaf03f6b104', 'Love the effort put into this.', 4, 9, 'tester', 'fa89b6e1-e715-4822-9891-4bdf6cb9c121'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '8d44b7e3-f951-437e-bfc1-bcaf45e0197b', 'Thanks for clarifying!', 8, 10, 'user10', '378cd3b2-05eb-48a7-b017-ace95767a4e7'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, '95cad3e4-d560-4e33-b810-cfa27a191344', 'Good addition to the topic.', 2, 7, 'support', '566db3e3-a432-4871-93aa-9469b9771d8f'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE, 'a3c4d2b9-c312-4adb-bef9-dbdc045193aa', 'Looking forward to more posts!', 5, 1, 'user1', 'ae098b3d-f560-47cb-a395-9d44a117cc6b');

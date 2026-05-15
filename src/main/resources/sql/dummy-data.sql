-- 1. Users
INSERT INTO users (user_id, email, password, status) VALUES
('admin', 'admin@example.com', 'hashed_password_example_1', 'JOIN'),
('user1', 'user1@example.com', 'hashed_password_example_2', 'JOIN'),
('user2', 'user2@example.com', 'hashed_password_example_3', 'JOIN'),
('dormant_user', 'dormant@example.com', 'hashed_password_example_4', 'DORMANT');

-- 2. Projects
INSERT INTO projects (project_id, name, status, admin_id) VALUES
(1, 'Minidooray Gateway Renewal', 'ACTIVE', 'admin'),
(2, 'Backend Refactoring', 'ACTIVE', 'user1'),
(3, 'Old Legacy Project', 'TERMINATED', 'admin');

-- 3. Project Members
INSERT INTO project_members (project_id, user_id) VALUES
(1, 'admin'),
(1, 'user1'),
(1, 'user2'),
(2, 'user1'),
(2, 'user2'),
(3, 'admin');

-- 4. Milestones
INSERT INTO milestones (milestone_id, project_id, name, start_date, end_date) VALUES
(1, 1, 'Phase 1: Setup', '2026-05-01', '2026-05-15'),
(2, 1, 'Phase 2: Development', '2026-05-16', '2026-06-30'),
(3, 2, 'DB Migration', '2026-06-01', '2026-06-15');

-- 5. Tags
INSERT INTO tags (tag_id, project_id, name) VALUES
(1, 1, 'Backend'),
(2, 1, 'Frontend'),
(3, 1, 'Bug'),
(4, 1, 'Urgent'),
(5, 2, 'Database'),
(6, 2, 'Optimization');

-- 6. Tasks
INSERT INTO tasks (task_id, project_id, milestone_id, title, content, writer_id, created_at) VALUES
(1, 1, 1, 'Project initialization', 'Setup Spring Boot project with necessary dependencies.', 'admin', NOW()),
(2, 1, 1, 'Design DB Schema', 'Create DDL for users, projects, and tasks.', 'admin', NOW()),
(3, 1, 2, 'Implement Task API', 'Develop REST API for task management.', 'user1', NOW()),
(4, 1, NULL, 'Fix login bug', 'User login occasionally fails due to session timeout.', 'user2', NOW()),
(5, 2, 3, 'Migrate to MySQL 8', 'Update driver and modify syntax for MySQL 8 compatibility.', 'user1', NOW());

-- 7. Task_Tags (Mapping)
INSERT INTO task_tags (task_id, tag_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(4, 3),
(4, 4), 
(5, 5),
(5, 6);

-- 8. Comments
INSERT INTO comments (comment_id, task_id, writer_id, content, created_at) VALUES
(1, 1, 'user1', 'I have pulled the initial setup. Looks good!', NOW()),
(2, 4, 'admin', 'Can someone check the Redis configuration?', NOW()),
(3, 4, 'user2', 'I am looking into it right now.', NOW());

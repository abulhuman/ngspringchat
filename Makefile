.PHONY: dev-db-up
dev-db-up:
	test -f .env || touch .env
	docker compose -f docker-compose.dev.yml --project-name ng_spring_chat_dev_db up --force-recreate db -d

.PHONY: dev-db-down
dev-db-down:
	docker compose --project-name ng_spring_chat_dev_db down

.PHONY: test-db-up
test-db-up:
	test -f .env || touch .env
	docker compose -f docker-compose.test.yml --project-name ng_spring_chat_test_db up --force-recreate db -d

.PHONY: test-db-down
test-db-down:
	docker compose --project-name ng_spring_chat_test_db down
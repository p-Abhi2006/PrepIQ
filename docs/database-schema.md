# PrepIQ Database Schema

## Overview

PrepIQ uses PostgreSQL to store users, skills, topics, questions,
tests, results, and submitted answers.

## Tables

### 1. users
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| name | VARCHAR | No |
| email | VARCHAR | No |
| password_hash | VARCHAR | No |
| created_at | TIMESTAMP | No |
| updated_at | TIMESTAMP | No |

### 2. skills
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| name | VARCHAR | No |
| description | VARCHAR | Yes |

### 3. topics
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| skill_id | BIGINT | No |
| name | VARCHAR | No |
| description | VARCHAR | Yes |

### 4. questions
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| question | VARCHAR | No |
| answer | VARCHAR | Yes |
| topic | VARCHAR | Yes |
| topic_id | BIGINT | Yes |

Note: `topic` is a legacy text column. `topic_id` is the foreign-key
relationship to the topics table. Both currently exist.

### 5. tests
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| title | VARCHAR | No |
| description | TEXT | Yes |
| duration_minutes | INTEGER | No |
| created_at | TIMESTAMP | No |
| updated_at | TIMESTAMP | No |

### 6. test_questions
| Column | Type | Nullable |
|---|---|---|
| test_id | BIGINT | No |
| question_id | BIGINT | No |
| question_order | INTEGER | No |
| marks | NUMERIC | No |

The table uses a composite primary key on `(test_id, question_id)`.

### 7. results
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| user_id | BIGINT | No |
| test_id | BIGINT | No |
| score | NUMERIC | No |
| total_marks | NUMERIC | No |
| started_at | TIMESTAMP | No |
| completed_at | TIMESTAMP | Yes |

### 8. user_answers
| Column | Type | Nullable |
|---|---|---|
| id | BIGINT | No |
| result_id | BIGINT | No |
| question_id | BIGINT | No |
| selected_answer | TEXT | Yes |
| is_correct | BOOLEAN | Yes |
| marks_awarded | NUMERIC | No |
| answered_at | TIMESTAMP | Yes |

## Foreign-Key Relationships

| Source | Foreign key | Referenced table |
|---|---|---|
| topics | skill_id | skills.id |
| questions | topic_id | topics.id |
| test_questions | test_id | tests.id |
| test_questions | question_id | questions.id |
| results | user_id | users.id |
| results | test_id | tests.id |
| user_answers | result_id | results.id |
| user_answers | question_id | questions.id |

## Notes

- Column details and foreign-key relationships were inspected in PostgreSQL.
- TIMESTAMP refers to `timestamp without time zone`.
- This document describes the current database, not every planned feature.
- Confirm unique constraints, check constraints, defaults, and indexes separately
  before documenting them as database guarantees.

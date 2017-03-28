DROP DATABASE IF EXISTS book;

CREATE DATABASE book
  DEFAULT CHARACTER SET utf8
  COLLATE utf8_general_ci;

USE book;

-- ----------------------------
--  Table structure for book
-- ----------------------------
DROP TABLE
IF EXISTS book;

CREATE TABLE book
(
  id                BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
  COMMENT '主键, 自增',
  name              VARCHAR(64)                           NOT NULL
  COMMENT '书名',
  author            VARCHAR(32)                           NOT NULL
  COMMENT '作者',
  intro             VARCHAR(512)                          NOT NULL
  COMMENT '简介',
  picture           VARCHAR(256)                          NOT NULL                    DEFAULT ''
  COMMENT '首图',
  url               INTEGER                               NOT NULL
  COMMENT '地址',
  new_chapter_url   INTEGER                               NOT NULL                    DEFAULT 0
  COMMENT '最新章节URL',
  new_chapter_title VARCHAR(128)                          NOT NULL                    DEFAULT ''
  COMMENT '最新章节名称',
  type              VARCHAR(32)                           NOT NULL                    DEFAULT ''
  COMMENT '类型',
  is_finished       TINYINT                               NOT NULL                    DEFAULT 0
  COMMENT '是否完结:{0:未完结, 1:已完结}',
  is_deleted        TINYINT                               NOT NULL                    DEFAULT 0
  COMMENT '逻辑删除:{0:未删除, 1:已删除}',
  created_time      TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  updated_time      TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间'
)
  COMMENT '书籍表';
CREATE UNIQUE INDEX id_UNIQUE
  ON book (id);
CREATE UNIQUE INDEX url_UNIQUE
  ON book (url);
CREATE INDEX author_ix
  ON book (author);

-- ----------------------------
--  Table structure for chapter
-- ----------------------------
DROP TABLE
IF EXISTS chapter;

CREATE TABLE chapter
(
  id           BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL
  COMMENT '主键, 自增',
  title        VARCHAR(128)                          NOT NULL
  COMMENT '章节名称',
  content      LONGTEXT                              NOT NULL
  COMMENT '章节内容',
  url          INTEGER                               NOT NULL
  COMMENT '章节地址',
  book_url     INTEGER                               NOT NULL
  COMMENT '书籍地址',
  is_deleted   TINYINT                               NOT NULL                    DEFAULT 0
  COMMENT '逻辑删除:{0:未删除, 1:已删除}',
  created_time TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  updated_time TIMESTAMP                             NOT NULL                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间'
)
  COMMENT '章节表';
CREATE UNIQUE INDEX id_UNIQUE
  ON chapter (id);
CREATE UNIQUE INDEX url_UNIQUE
  ON chapter (url);
CREATE INDEX book_url_ix
  ON chapter (book_url);
CREATE INDEX create_ix
  ON chapter (created_time);


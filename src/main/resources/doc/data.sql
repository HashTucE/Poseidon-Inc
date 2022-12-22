INSERT INTO users (fullname, username, password, role)
VALUES ('Administrator', 'admin', '$2a$12$0oQ3udeHKMrmOe8Oo7FUVeA0.82cBXXu5xYkCpYBx5.OrG63zYNha', 'ADMIN');
INSERT INTO users (fullname, username, password, role)
VALUES ('User', 'user', '$2a$12$0oQ3udeHKMrmOe8Oo7FUVeA0.82cBXXu5xYkCpYBx5.OrG63zYNha', 'USER');

insert into bids(account, type, bidQuantity) values("account", "type", 1.0);
insert into bids(account, type, bidQuantity) values("account", "type", 1.0);
insert into bids(account, type, bidQuantity) values("account", "type", 1.0);

insert into trades(account, type, buyQuantity) values("account", "type", 1.0);
insert into trades(account, type, buyQuantity) values("account", "type", 1.0);
insert into trades(account, type, buyQuantity) values("account", "type", 1.0);

insert into curves(curveId, term, value) values(1, "term", 1.0);
insert into curves(curveId, term, value) values(1, "term", 1.0);
insert into curves(curveId, term, value) values(1, "term", 1.0);

insert into ratings(moodysRating, sandPRating, fitchRating, orderNumber) values("a", "a", "a", 1);
insert into ratings(moodysRating, sandPRating, fitchRating, orderNumber) values("a", "a", "a", 1);
insert into ratings(moodysRating, sandPRating, fitchRating, orderNumber) values("a", "a", "a", 1);

insert into rules(name, description, json, template, sqlStr, sqlPart) values("a", "a", "a", "a", "a", "a");
insert into rules(name, description, json, template, sqlStr, sqlPart) values("a", "a", "a", "a", "a", "a");
insert into rules(name, description, json, template, sqlStr, sqlPart) values("a", "a", "a", "a", "a", "a");
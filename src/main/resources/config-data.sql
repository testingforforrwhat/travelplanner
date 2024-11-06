INSERT INTO users (username, email, password_hash, avatar_url) VALUES
                                                                   ('john_doe', 'john@example.com', '$2a$10$abcdefghijklmnopqrstuvwxyz123457', 'https://example.com/avatars/john.jpg'),
                                                                   ('jane_smith', 'jane@example.com', '$2a$10$abcdefghijklmnopqrstuvwxyz123458', 'https://example.com/avatars/jane.jpg');

INSERT INTO cities (name, name_en, country, country_code, latitude, longitude, timezone, alpha_level, description, image_url) VALUES
    ('洛杉矶', 'Los Angeles', '美国', 'US', 34.0522, -118.2437, 'America/Los_Angeles', 'alpha+', '洛杉矶是美国加利福尼亚州最大的城市，以其好莱坞娱乐产业而闻名。', 'https://example.com/los_angeles.jpg');

INSERT INTO points_of_interest (
    city_id, name, name_en, category, description, address,
    latitude, longitude, avg_visit_time, opening_hours, price_level, rating, image_urls
) VALUES
      (1, '好莱坞星光大道', 'Hollywood Walk of Fame', '景点',
       '好莱坞星光大道是洛杉矶著名的旅游景点，拥有众多明星的名字。',
       'Hollywood Blvd, Los Angeles, CA 90028, USA',
       34.1015, -118.3265, 60,
       '{"monday": {"open": "00:00", "close": "23:59"},
         "tuesday": {"open": "00:00", "close": "23:59"},
         "wednesday": {"open": "00:00", "close": "23:59"},
         "thursday": {"open": "00:00", "close": "23:59"},
         "friday": {"open": "00:00", "close": "23:59"},
         "saturday": {"open": "00:00", "close": "23:59"},
         "sunday": {"open": "00:00", "close": "23:59"}}',
       3, 4.3,
       '["https://example.com/hollywood1.jpg", "https://example.com/hollywood2.jpg"]'
      ),
      (1, '格里菲斯天文台', 'Griffith Observatory', '景点',
       '格里菲斯天文台提供了洛杉矶市区的壮丽景色，是一个受欢迎的观光地点。',
       '2800 E Observatory Rd, Los Angeles, CA 90027, USA',
       34.1184, -118.3004, 120,
       '{"monday": {"open": "12:00", "close": "22:00"},
         "tuesday": {"open": "12:00", "close": "22:00"},
         "wednesday": {"open": "12:00", "close": "22:00"},
         "thursday": {"open": "12:00", "close": "22:00"},
         "friday": {"open": "12:00", "close": "22:00"},
         "saturday": {"open": "10:00", "close": "22:00"},
         "sunday": {"open": "10:00", "close": "22:00"}}',
       4, 4.7,
       '["https://example.com/griffith1.jpg", "https://example.com/griffith2.jpg"]'
      );

INSERT INTO trips (user_id, city_id, title, days, start_date, status, notes) VALUES
    (1, 1, '洛杉矶5日游', 5, '2024-05-01', 'active', '探索洛杉矶的主要景点和文化。');

INSERT INTO trip_points (
    trip_id, poi_id, day_number, visit_order, planned_arrival_time, planned_departure_time, notes
) VALUES
      (1, 1, 1, 1, '09:00', '11:00', '参观好莱坞星光大道，拍照留念'),
      (1, 2, 1, 2, '12:00', '15:00', '在格里菲斯天文台欣赏城市全景');
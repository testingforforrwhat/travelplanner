DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS destinations CASCADE;
DROP TABLE IF EXISTS points_of_interest CASCADE;
DROP TABLE IF EXISTS trips CASCADE;
DROP TABLE IF EXISTS trip_points;


CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,
                       avatar_url VARCHAR(255),
                       is_active BOOLEAN DEFAULT true,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS '用户信息表';
COMMENT ON COLUMN users.id IS '用户ID';
COMMENT ON COLUMN users.username IS '用户名';
COMMENT ON COLUMN users.email IS '电子邮箱';
COMMENT ON COLUMN users.password_hash IS '密码哈希值';
COMMENT ON COLUMN users.avatar_url IS '头像URL';
COMMENT ON COLUMN users.is_active IS '是否活跃';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.updated_at IS '更新时间';


CREATE TABLE authorities
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE cities (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        name_en VARCHAR(100) NOT NULL,
                        country VARCHAR(100) NOT NULL,
                        country_code CHAR(2) NOT NULL,
                        latitude DECIMAL(10, 8) NOT NULL,
                        longitude DECIMAL(11, 8) NOT NULL,
                        timezone VARCHAR(50) NOT NULL,
                        alpha_level VARCHAR(10) NOT NULL,
                        description TEXT,
                        image_url VARCHAR(255),
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE cities IS '城市信息表';
COMMENT ON COLUMN cities.id IS '城市ID';
COMMENT ON COLUMN cities.name IS '城市名称（中文）';
COMMENT ON COLUMN cities.name_en IS '城市名称（英文）';
COMMENT ON COLUMN cities.country IS '所属国家';
COMMENT ON COLUMN cities.country_code IS '国家代码(ISO 3166-1 alpha-2)';
COMMENT ON COLUMN cities.latitude IS '纬度';
COMMENT ON COLUMN cities.longitude IS '经度';
COMMENT ON COLUMN cities.timezone IS '时区';
COMMENT ON COLUMN cities.alpha_level IS '城市等级';
COMMENT ON COLUMN cities.description IS '城市描述';
COMMENT ON COLUMN cities.image_url IS '城市图片URL';


CREATE TABLE points_of_interest (
                                    id SERIAL PRIMARY KEY,
                                    city_id INTEGER REFERENCES cities(id),
                                    name VARCHAR(100) NOT NULL,
                                    name_en VARCHAR(100) NOT NULL,
                                    category VARCHAR(50) NOT NULL,
                                    description TEXT,
                                    address TEXT NOT NULL,
                                    latitude DECIMAL(10, 8) NOT NULL,
                                    longitude DECIMAL(11, 8) NOT NULL,
                                    avg_visit_time INTEGER NOT NULL CHECK (avg_visit_time > 0),
                                    opening_hours JSONB NOT NULL,
                                    price_level INTEGER CHECK (price_level BETWEEN 1 AND 5),
                                    rating DECIMAL(2,1) CHECK (rating BETWEEN 0 AND 5),
                                    image_urls JSONB,
                                    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE points_of_interest IS '景点信息表';
COMMENT ON COLUMN points_of_interest.id IS '景点ID';
COMMENT ON COLUMN points_of_interest.city_id IS '所属城市ID';
COMMENT ON COLUMN points_of_interest.name IS '景点名称（中文）';
COMMENT ON COLUMN points_of_interest.name_en IS '景点名称（英文）';
COMMENT ON COLUMN points_of_interest.category IS '景点类别';
COMMENT ON COLUMN points_of_interest.description IS '景点描述';
COMMENT ON COLUMN points_of_interest.address IS '地址';
COMMENT ON COLUMN points_of_interest.latitude IS '纬度';
COMMENT ON COLUMN points_of_interest.longitude IS '经度';
COMMENT ON COLUMN points_of_interest.avg_visit_time IS '平均参观时间（分钟）';
COMMENT ON COLUMN points_of_interest.opening_hours IS '营业时间';
COMMENT ON COLUMN points_of_interest.price_level IS '价格等级（1-5）';
COMMENT ON COLUMN points_of_interest.rating IS '评分（0-5）';
COMMENT ON COLUMN points_of_interest.image_urls IS '景点图片URL列表';

CREATE TABLE destinations (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        location VARCHAR(100) NOT NULL,
                        description TEXT,
                        image_url VARCHAR(255),
                        averageRating DECIMAL(2,1) CHECK (averageRating BETWEEN 0 AND 5)
);

COMMENT ON TABLE destinations IS '目的地信息表';
COMMENT ON COLUMN destinations.id IS '目的地ID';
COMMENT ON COLUMN destinations.name IS '目的地名称';
COMMENT ON COLUMN destinations.location IS '目的地位置';
COMMENT ON COLUMN destinations.description IS '目的地描述';
COMMENT ON COLUMN destinations.image_url IS '目的地图片URL';
COMMENT ON COLUMN destinations.averageRating IS '目的地评分';

CREATE TABLE trips (
                       id SERIAL PRIMARY KEY,
                       user_id INTEGER REFERENCES users(id),
                       destination_id INTEGER REFERENCES destinations(id),
                       title VARCHAR(100) NOT NULL,
                       days INTEGER NOT NULL CHECK (days BETWEEN 1 AND 15),
                       start_date DATE NOT NULL,
                       status VARCHAR(20) DEFAULT 'draft',
                       notes TEXT,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE trips IS '行程表';
COMMENT ON COLUMN trips.id IS '行程ID';
COMMENT ON COLUMN trips.user_id IS '用户ID';
COMMENT ON COLUMN trips.destination_id IS '目的地城市ID';
COMMENT ON COLUMN trips.title IS '行程标题';
COMMENT ON COLUMN trips.days IS '行程天数';
COMMENT ON COLUMN trips.start_date IS '开始日期';
COMMENT ON COLUMN trips.status IS '行程状态';
COMMENT ON COLUMN trips.notes IS '行程备注';


CREATE TABLE trip_points (
                             id SERIAL PRIMARY KEY,
                             trip_id INTEGER REFERENCES trips(id),
                             poi_id INTEGER REFERENCES points_of_interest(id),
                             day_number INTEGER NOT NULL CHECK (day_number >= 1),
                             visit_order INTEGER NOT NULL,
                             planned_arrival_time TIME,
                             planned_departure_time TIME,
                             notes TEXT,
                             created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE trip_points IS '行程景点关联表';
COMMENT ON COLUMN trip_points.id IS '行程景点ID';
COMMENT ON COLUMN trip_points.trip_id IS '所属行程ID';
COMMENT ON COLUMN trip_points.poi_id IS '景点ID';
COMMENT ON COLUMN trip_points.day_number IS '行程第几天';
COMMENT ON COLUMN trip_points.visit_order IS '当天访问顺序';
COMMENT ON COLUMN trip_points.planned_arrival_time IS '计划到达时间';
COMMENT ON COLUMN trip_points.planned_departure_time IS '计划离开时间';
COMMENT ON COLUMN trip_points.notes IS '备注';
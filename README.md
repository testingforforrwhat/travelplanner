

# Travel Planner 🌍

A modern travel planning platform built with React/Vue and Spring Boot that helps travelers design their routes efficiently using Google/Tencent Maps integration.

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Version](https://img.shields.io/badge/version-1.0.0-green.svg)

## 🌟 Features

- **Single City Route Planning**: Design travel routes within international alpha-level cities
- **Smart Route Optimization**: Efficiently plan your visits with our route optimization algorithm
- **POI Search & Marking**: Search and mark points of interest directly on the map
- **Trip Management**: Create, edit, and share your travel plans
- **Social Interaction**: Share experiences and interact with other travelers

## 🛠️ Tech Stack

### Frontend

- React.js / Vue.js
- Redux Toolkit / Vuex
- Tailwind CSS / Ant Design / Element Plus
- Axios / Fetch
- Google/Tencent Maps API

### Backend

- Spring Boot
- PostgreSQL
- Redis/Caffeine Cache
- JWT Authentication
- Google/Tencent Maps Services

### DevOps

- Docker
- Nginx
- Google Cloud

## 🏗️ Project Structure

```bash
├── frontend/                 # React frontend application
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── services/       # API services
│   │   ├── store/          
│   │   ├── router/          
│   │   ├── utils/          # Utility functions
│   │   └── assets/         # Static assets
│   └── package.json
│
├── backend/                  # Spring Boot backend application
│   ├── src/
│   │   └── main/
│   │       ├── java/com/travelplanner/
│   │       │   ├── config/     # Configuration files
│   │       │   ├── controller/ # REST controllers
│   │       │   ├── service/    # Business logic
│   │       │   ├── repository/ # Data access
│   │       │   ├── model/      # Data models
│   │       │   └── util/       # Utilities
│   │       └── resources/
│   └── pom.xml
│
└── docker/                   # Docker configuration files
```

## 🚀 Getting Started

### Prerequisites

- Node.js >= 16
- Java 17
- PostgreSQL >= 13
- Docker (optional)

### Local Development Setup

1. **Clone the repository**

```bash
git clone https://github.com/testingforforrwhat/travelplanner.git
cd travelplanner

git clone https://github.com/testingforforrwhat/travelplanner_vue.git
cd travelplanner_vue
```

2. **Setup Backend**

```bash
cd backend
# Configure database in application.properties
./gradle spring-boot:run
```

3. **Setup Frontend**

```bash
cd frontend
npm install
npm build
```

4. **Access the application**

- Frontend: http://localhost:8081
- Backend API: http://localhost:8080

### Docker Setup

```bash
docker-compose up -d
```

## 📝 API Documentation

### Core Endpoints

#### User Management

- `POST /api/v1/users/register` - User registration
- `POST /api/v1/users/login` - User login
- `GET /api/v1/users/profile` - Get user profile

#### Destinations

- `GET /api/v1/destinations` - List destinations
- `GET /api/v1/destinations/{id}` - Get destination details
- `GET /api/v1/destinations/search` - Search destinations

#### Trip Planning

- `POST /api/v1/trips` - Create trip
- `GET /api/v1/trips` - List trips
- `PUT /api/v1/trips/{id}` - Update trip

For complete API documentation, visit `/swagger-ui.html` after starting the backend server.

## 🔒 Security Features

- JWT-based authentication and authorization
- HTTPS encryption for data transmission
- Input validation and sanitization
- Rate limiting for API endpoints
- XSS and CSRF protection
- SQL injection prevention
- Password hashing using BCrypt

## 🧪 Testing

### Running Tests

#### Backend Tests

```bash
cd backend
./gradle test
```

#### Frontend Tests

```bash
cd frontend
npm test
```

### Test Coverage

Generate test coverage reports:

```bash
# Backend
./gradle verify

# Frontend
npm run test:coverage
```

## 📊 Database Schema

### Core Tables

- `users` - User information
- `cities` - City information
- `points_of_interest` - POI details
- `trips` - Trip planning data
- `trip_points` - Trip-POI associations

For complete schema details, refer to `docs/database.md`

## 🔧 Configuration

### Environment Variables

#### Backend

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/travelplanner
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000

# Google Maps
GOOGLE_MAPS_API_KEY=your_api_key
```

#### Frontend

```env
Vue_APP_API_URL=http://localhost:8080/api
Vue_APP_GOOGLE_MAPS_KEY=your_google_maps_key
```

## 🚀 Deployment

### Production Build

#### Backend

```bash
./gradle clean package -Pprod
```

#### Frontend

```bash
npm run build
```

### Docker Deployment

```bash
# Build images
docker-compose -f docker-compose.prod.yml build

# Deploy
docker-compose -f docker-compose.prod.yml up -d
```

## 📈 Performance Optimization

- Database query optimization
- Redis/Caffeine caching for frequently accessed data
- Frontend bundle optimization
- Image optimization and lazy loading
- API response compression
- CDN integration for static assets

## 🛣️ Roadmap

### Phase 1 (Current)

- ✅ Basic route planning
- ✅ POI search and marking
- ✅ User authentication

### Phase 2 (Upcoming)

- 🔄 Personalized recommendations
- 🔄 Social sharing features
- 🔄 Multi-city route planning

### Phase 3 (Future)

- 📅 Real-time traffic integration
- 📅 Mobile app development
- 📅 AI-powered trip suggestions

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- Project Lead: [Name](https://github.com/username)
- Frontend Lead: [Name](https://github.com/username)
- Backend Lead: [Name](https://github.com/username)
- UI/UX Designer: [Name](https://github.com/username)

## 📞 Support

- Documentation: [docs/](docs/)
- Issue Tracker: [GitHub Issues](https://github.com/yourusername/travel-planner/issues)
- Email: support@travelplanner.com

## 🙏 Acknowledgments

- Google/Tencent Maps Platform
- OpenStreetMap Contributors
- [Other libraries and resources used in the project]

---

Made with ❤️ by the Travel Planner Team-




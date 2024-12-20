# Simple Blog Post RESTful API

This is simple RESTful API for managing a simple blogging platform. The core functionality of this platform includes managing blog posts and their associated comments.

## Requirements

The code is production-ready-like, and it is not intended to be in full. This project took not more than 4 hours of work.

### Data Models
- **BlogPost**: Contains a title and content. Each post can have multiple associated comments.
- **Comment**: Related to a BlogPost.

## API Endpoints

### **GET** `/api/posts`
- Returns a list of all blog posts, including their titles and the number of associated comments.

### **POST** `/api/posts`
- Creates a new blog post.

### **GET** `/api/posts/{id}`
- Retrieves a specific blog post by its ID, including its title, content, and a list of associated comments.

### **POST** `/api/posts/{id}/comments`
- Adds a new comment to a specific blog post.

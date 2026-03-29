pipeline {
    agent any
    environment {
        IMAGE_NAME = "java-k8s-demo"
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'KarthickSuresh09',
                    url: 'https://github.com/KarthickSuresh09/java-k8s-demo.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }
        stage('Docker Run') {
            steps {
                sh "docker stop spring-app || true"
                sh "docker rm spring-app || true"
                sh """
                    docker run -d \
                      --name spring-app \
                      -p 8080:8080 \
                      --network host \
                      -e DB_HOST=localhost \
                      -e DB_NAME=studentDB \
                      -e DB_USER=root \
                      -e DB_PASS=Root@123 \
                      ${IMAGE_NAME}:latest
                """
            }
        }
        stage('Health Check') {
            steps {
                sh '''
                    echo "Waiting for app to start..."
                    sleep 5
                    docker logs spring-app | tail -20
                '''
            }
        }
    }
    post {
        success {
            echo "Build #${BUILD_NUMBER} deployed successfully!"
            sh "curl -s http://localhost:8080/actuator/health | head -20 || echo 'App starting...'"
        }
        failure {
            echo "Build #${BUILD_NUMBER} failed. Check logs."
            sh "docker logs spring-app || echo 'No container logs'"
        }
    }
}

pipeline {
    agent any

    environment {
        AWS_REGION   = 'ap-south-2'
        ECR_REGISTRY = 'XXXXXXXXXXXX.dkr.ecr.ap-south-2.amazonaws.com'
        ECR_REPO     = 'java-k8s-demo'
        IMAGE_TAG    = "${BUILD_NUMBER}"
        FULL_IMAGE   = "${ECR_REGISTRY}/${ECR_REPO}:${IMAGE_TAG}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'github-credentials',
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
                sh "docker build -t ${FULL_IMAGE} ."
                sh "docker tag ${FULL_IMAGE} ${ECR_REGISTRY}/${ECR_REPO}:latest"
            }
        }

        stage('Push to ECR') {
            steps {
                withAWS(credentials: 'aws-credentials', region: "${AWS_REGION}") {
                    sh """
                        aws ecr get-login-password --region ${AWS_REGION} | \
                        docker login --username AWS --password-stdin ${ECR_REGISTRY}
                        docker push ${FULL_IMAGE}
                        docker push ${ECR_REGISTRY}/${ECR_REPO}:latest
                    """
                }
            }
        }
    }

    post {
        success { echo "Build #${BUILD_NUMBER} pushed to ECR successfully!" }
        failure { echo "Build #${BUILD_NUMBER} failed. Check logs." }
    }
}

pipeline {
    agent any

    tools {
        maven 'Maven-3.9.16'
    }

    environment {
        IMAGE_NAME = 'portfolio-app'
        IMAGE_TAG  = "1.0.${BUILD_NUMBER}"
        CONTAINER_NAME = 'portfolio-container'
        APP_PORT = '8085'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the application...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running Selenium UI tests...'
                bat 'mvn test -Dtest=SeleniumTest'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Building Docker image: ${IMAGE_NAME}:${IMAGE_TAG}"
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                bat "docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest"
            }
        }

        stage('Docker Deploy') {
            steps {
                echo 'Stopping old container if running...'
                bat "docker stop ${CONTAINER_NAME} & echo stopped"
                bat "docker rm ${CONTAINER_NAME} & echo removed"

                echo "Starting new container: ${CONTAINER_NAME}"
                bat "docker run -d -p ${APP_PORT}:${APP_PORT} --name ${CONTAINER_NAME} ${IMAGE_NAME}:latest"

                echo 'Waiting for app to start...'
                bat 'timeout /t 15 /nobreak'
            }
        }

        stage('Health Check') {
            steps {
                echo 'Checking app health...'
                bat "curl -f http://localhost:${APP_PORT}/actuator/health"
                bat "docker ps --filter name=${CONTAINER_NAME}"
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline SUCCESS - App running at http://localhost:${APP_PORT}"
        }
        failure {
            echo '❌ Pipeline FAILED - Check logs above'
        }
    }
}
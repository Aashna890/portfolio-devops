pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
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

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Archive') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }

        stage('Health Check') {
            steps {
                echo 'Build complete - artifact ready for deployment'
                echo "Artifact: portfolio-0.0.1-SNAPSHOT.jar"
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline SUCCESS - Build is healthy'
        }
        failure {
            echo '❌ Pipeline FAILED - Check logs above'
        }
    }
}
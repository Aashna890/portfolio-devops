pipeline {
    agent any

    tools {
        maven 'Maven-3.9.16'
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
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline SUCCESS - All tests passed'
        }
        failure {
            echo '❌ Pipeline FAILED - Check logs above'
        }
    }
}
pipeline{
    agent any
    tools{
        maven 'M3'
        dockerTool 'docker'
    }
    stages{
        stage('Build Maven'){
            steps{
                sh 'git config --global --add safe.directory "*"'
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/shoaibpki/abctelecomBE.git']])
                sh 'mvn install -DskipTests'
            }
        }
        stage('Build image'){
            steps{
                sh 'docker build -t shoaibpki/abctelecom:${BUILD_NUMBER} .'
            }
        }
        stage('Push image on dockerhub'){
            steps{
                withCredentials([string(credentialsId: 'dockerHubPassword', variable: 'dockerPassword')]) {
                    sh 'docker login -u shoaibpki -p ${dockerPassword}'
                }
                sh 'docker push shoaibpki/abctelecom:${BUILD_NUMBER}'
            }
        }
    }
}
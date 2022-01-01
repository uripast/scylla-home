node (python3){
    cleanWs()

    environment {
        def repo_name = 'Scylladb'
        def build = manager.build // use post action plugin to calculate pass/fail rate
        def USER = "uripa"
        def PASS = "xxxx"
    }
    stage("checkout git")
            {
                sh "git clone https://github.com/scylladb/scylla.git"
                sh "cd scylla"
                sh "git submodule update --init --recursive"
            }

    stage('build and prepare Docker image') {
        dir(repo_name){
            sh "sudo ./install-dependencies.sh"
            sh "./tools/toolchain/dbuild ./configure.py"
            sh "./tools/toolchain/dbuild ninja build/release/scylla"
            sh "./tools/toolchain/dbuild ./build/release/scylla --developer-mode 1"
            sh "docker login USER PASS"
            sh "docker build -t scylla ."
        }

	
    }
    stage("Run docker container with unit tests"){
        sh('docker run --rm -it scylla') // docker run conssits of test.py that run the unit tests

    }
    stage("List pass/fail totals")


    int total = build.getTestResultAction().getTotalCount()
    int failed = build.getTestResultAction().getFailCount()
    int skipped = build.getTestResultAction().getSkipCount()
    // can also be accessed like build.testResultAction.failCount

    manager.listener.logger.println('Total: ' + total)
    manager.listener.logger.println('Failed: ' + failed)
    manager.listener.logger.println('Skipped: ' + skipped)
    manager.listener.logger.println('Passed: ' + (total - failed - skipped))
}
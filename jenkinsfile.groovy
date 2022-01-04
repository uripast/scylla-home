node (python3) {
    cleanWs()

    stage("checkout git")
            {
                sh "git clone https://github.com/scylladb/scylla.git"


            }

    stage('build and prepare Docker image') {
        dir(repo_name) {
            sh "cd scylla"
            sh "git submodule update --init --recursive"
            sh "./tools/toolchain/dbuild ./configure.py --mode='dev' "

        }
        stage("Run unit tests") {
            // scylla consists of test.py that run the unit tests
            sh "./tools/toolchain/dbuild ./test.py --mode={debug,release}>>output.txt"
            sh "cat output.txt"
        }
    }
}

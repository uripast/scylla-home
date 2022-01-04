FROM ubuntu:20.04

#prepare and install scylla
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 5e08fbd8b5d6ec9c
sudo curl -L --output /etc/apt/sources.list.d/scylla.list http://downloads.scylladb.com/deb/ubuntu/scylla-4.5-$(lsb_release -s -c).list
sudo apt-get update
sudo apt-get install -y scylla

#update java 8

sudo apt-get update
sudo apt-get install -y openjdk-8-jre-headless
sudo update-java-alternatives --jre-headless -s java-1.8.0-openjdk-amd64

#set scylla 
sudo scylla_setup

#finally run scylla
sudo systemctl start scylla-server

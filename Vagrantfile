# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  
  config.vm.box = "hashicorp/precise64"
  config.vm.box_check_update = true
  config.vm.network "forwarded_port", guest: 8080, host: 80
  # config.vm.network "private_network", ip: "10.0.0.50"

  # config.vm.network "public_network"
  # config.vm.synced_folder "../data", "/vagrant_data"

  config.vm.provision "shell", path: "src/vagrant/java8.sh"
  config.vm.provision "shell", path: "src/vagrant/mongodb.sh"
  config.vm.provision "shell", path: "src/vagrant/maven.sh"
end

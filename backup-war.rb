#!/usr/bin/env ruby
require 'rubygems'
require 'pathname'
require 'net/ssh'
require 'net/scp'

hostname = '10.11.12.53'
username = 'jboss-deployer'
password = 'password'

project = 'continuous-deployment-web'

$localDir = 'continuous-deployment-web/target/'
$backupDir = $localDir + 'backup/'
$remoteDir = '~/jboss-6.0.0.Final/server/default/deploy/'

def isDirectory(filename)
  /\/$/.match(filename)
end

def getDeployedFiles()
  ssh.exec!("ls -dp #{$remoteDir}#{project}*.war").split
end

def backupFile(sourceFile)
  filename = Pathname.new(sourceFile).basename.to_s
  
  puts "Backing up: " + filename + "..."
  
  unless File.exists?($backupDir)
    Dir.mkdir($backupDir)
  end
  
  unless isDirectory(sourceFile)
    ssh.scp.download!(sourceFile, $backupDir + filename)
  end
end

def backupExistingDeployment(ssh, project)
  getDeployedFiles().each do |sourceFile|
    backupFile(sourceFile)
  end
end

def deploy(ssh, project)
  puts "Deploying " + project + "..."
end

Net::SSH.start(hostname, username, :password => password) do |ssh|
  backupExistingDeployment(ssh, project)
  
  deploy(ssh, project)
end

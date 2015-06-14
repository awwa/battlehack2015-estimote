require 'sinatra'
require 'sinatra/base'
#require 'sinatra/rocketio'
require 'json'
require 'dotenv'
require 'logger'
#require 'mongo'
require 'rubygems'
require 'bundler'

require "./src/mailer"
require "./src/setting"
require 'pusher'

Bundler.require

#require './src/configure'
#require './src/sendgrid'
#require './src/models/mail'
#require './src/controls/db_access'
#require './src/controls/mail_collection'
require File.join(File.dirname(__FILE__), 'src', 'main')

#use Rack::Reloader, 0
run Main

#

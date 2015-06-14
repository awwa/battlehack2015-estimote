# -*- encoding: utf-8 -*-

require 'sendgrid_ruby'
require 'sendgrid_ruby/version'
require 'sendgrid_ruby/email'

class Mailer

  def initialize
    @logger = Logger.new(STDOUT)
    @setting = Setting.new
  end

  def send(data)
    sendgrid = SendgridRuby::Sendgrid.new(
      @setting.sendgrid_username, @setting.sendgrid_password)
    sendgrid.send(get_email(data))
  end

  def get_email(data)
    email = SendgridRuby::Email.new
    email.set_tos(@setting.tos)
    email.set_from(@setting.from)
    email.set_subject("mac: #{data['mac']}")
    email.set_text("mac: #{data['mac']}\r\nacc: #{data['acc']}")
    @logger.info JSON.pretty_generate(email.to_web_format)
    email
  end
end

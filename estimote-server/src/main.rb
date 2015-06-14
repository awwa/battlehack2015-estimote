# -*- encoding: utf-8 -*-

class Main < Sinatra::Base

  configure :production, :development do
    begin
      enable :logging
      #setting = Setting.new

      # if setting.basic_auth_username.length > 0 then
      #   use Rack::Auth::Basic do |username, password|
      #     username == setting.basic_auth_username && password == setting.basic_auth_password
      #   end
      # end

      # # init sendgrid
      # Configure.init_sendgrid(setting)
    rescue => e
      puts e.backtrace
      puts e.inspect
    end
  end

  helpers do
    # name=>valueの配列を連想配列に変換
    def to_kv(data)
      ret = {}
      data.each do |kv|
        ret[kv["name"]] = kv["value"]
      end
      ret
    end

    def notify(data)
      res = ""
      begin
        request.body.rewind
        body = request.body.read
        if body.length > 0 then
          Pusher.url = ENV['PUSHER_URL']

          Pusher['test_channel'].trigger('my_event', {
            message: data
          })
        end
      rescue => e
        logger.error e.backtrace
        logger.error e.inspect
        res = e.inspect
      end
      res
    end
  end

  get '/' do
    "hello!"

  end

  get '/subscribe' do
    erb :subscribe
  end

  post '/notify' do
    notify('hello world2')
  end

  post '/send' do
    res = ""
    begin
      request.body.rewind
      body = request.body.read
      if body.length > 0 then
        data = JSON.parse(body)
        logger.info "data: #{data.inspect}"
        puts "data: #{data.inspect}"
        mailer = Mailer.new
        #res = JSON.pretty_generate(mailer.send(to_kv(data)))
        if data["key"] == ENV['KEY']
          notify(data.inspect)
          return res = JSON.pretty_generate(mailer.send(data))
        end
        'yeah!!'
      end
    rescue => e
      logger.error e.backtrace
      logger.error e.inspect
      res = e.inspect
    end
    res
  end
end

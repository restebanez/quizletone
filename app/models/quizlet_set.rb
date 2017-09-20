require 'rest-client'
require 'yaml'
require "awesome_print"

class QuizletSet
  USERNAME = 'Rodrigo_Estebanez'
  CLIENT_ID = 'G23bfDAV9M'
  ACCESS_TOKEN = 'TuYX0AE0CEYa2qExHj4nDstW8Gf0BQYAyZTrSe8P'
  AUTHORIZATION_BEARER = "Bearer #{ACCESS_TOKEN}"
  BASE_URL = 'https://api.quizlet.com/2.0/users'
  USERS_URL = "#{BASE_URL}/#{USERNAME}"

  def self.list
    response_body = RestClient.get(USERS_URL, {params: {client_id: CLIENT_ID}}).body
    YAML.load(response_body)["sets"].map do |set|
      set.slice("id", "url", "title")
    end
  end
end

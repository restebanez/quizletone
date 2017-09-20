require 'rest-client'
require 'json'
require "awesome_print"

class QuizletSet
  USERNAME = 'Rodrigo_Estebanez'
  CLIENT_ID = 'G23bfDAV9M'
  ACCESS_TOKEN = 'TuYX0AE0CEYa2qExHj4nDstW8Gf0BQYAyZTrSe8P'
  AUTHORIZATION_BEARER = "Bearer #{ACCESS_TOKEN}"
  BASE_URL = 'https://api.quizlet.com/2.0/users'
  USERS_URL = "#{BASE_URL}/#{USERNAME}"

  def self.list
    JSON.parse(
        RestClient.get(USERS_URL, {params: {client_id: CLIENT_ID}}).body)
  end
end

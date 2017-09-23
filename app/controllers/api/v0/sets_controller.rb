module Api
  module V0
    class SetsController < ApplicationController
      def index
        render(json: MultiJson.encode(sets: QuizletSet.list))
      end
    end
  end
end

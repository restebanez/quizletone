class SetsController < ApplicationController
  def index
    render plain: QuizletSet.list
  end

  def show
  end
end

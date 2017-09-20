class SetsController < ApplicationController
  def index
    @quizlet_sets = QuizletSet.list
  end

  def show
  end
end

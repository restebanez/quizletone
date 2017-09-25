Rails.application.routes.draw do
  resources :sets, only: [:index, :show]
  get 'reframe_sets/index'
  get 'react_sets/index'


  root 'react_sets#index'

  namespace :api do
    namespace :v0 do
      resources :sets, only: [:index]
    end
  end

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end

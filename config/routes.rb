Rails.application.routes.draw do
  #get 'sets/index'

  #get 'sets/show'
  resources :sets, only: [:index, :show]

  root 'sets#index'

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end

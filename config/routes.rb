Rails.application.routes.draw do
  resources :sets, only: [:index, :show]

  root 'sets#index'

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end

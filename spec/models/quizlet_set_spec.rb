require "rails_helper"

RSpec.describe QuizletSet, :type => :model do
  it "test" do
    response_body_hash = {"sets" => [{"url" => "/11111/easy", "id" => 11111, "title" => "Easy", "unneccesary" => "bar"},
                                     {"url" => "/22222/non-easy", "id" => 22222, "title" => "Non-easy", "unneccesary" => "foo"}]}
    expect(QuizletSet.new.extract_attrs(response_body_hash).first).to eq({"url" => "/11111/easy", "id" => 11111, "title" => "Easy"})
  end
end
workflow "Maven build" {
  on = "push"
  resolves = ["Build"]
}

action "Build" {
  uses = "LucaFeger/action-maven-cli@9d8f23af091bd6f5f0c05c942630939b6e53ce44"
  args = "clean package"
}

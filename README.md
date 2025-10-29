![License](https://img.shields.io/github/license/octocat/Hello-World)
![Stars](https://img.shields.io/github/stars/octocat/Hello-World?style=social)
[![CI](https://github.com/octocat/Hello-World/actions/workflows/ci.yml/badge.svg)](https://github.com/octocat/Hello-World/actions)

# Chatbot Setup
To set up the chatbot, you must first download and run OLlama's gemma3:1b model.
## Download
1. Go to https://ollama.com/download
2. Select download for your specific OS
### Windows/Mac
  1. Download the install wizard
  2. Run the wizard
  3. Once the wizard is finished, the Ollama interface should begin running
  4. Select gemma3:1b from the list of LOCAL (non-cloud) options
  5. Enter a single prompt and wait for gemma3:1b to finish downloading
### Linux
  1. curl using the given prompt
  2. ollama pull gemma3:1b
  3. ollama run gemma3:1b
## Application
Now that the model is running, any request sent to /api/chat is sent to the ollama model!
Ensure you are logged in, and that the json is in the form {"message":""}.
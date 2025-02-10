# refactor.py

import requests
import os
import sys

API_URL = "https://api-inference.huggingface.co/models/mistralai/Mixtral-8x7B-Instruct-v0.1"
HEADERS = {"Authorization": "Bearer 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"}

def query(payload):
    try:
        response = requests.post(API_URL, headers=HEADERS, json=payload)
        response.raise_for_status()  # Raise an exception for HTTP errors
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error querying the API: {e}")
        return None

def refactor_code(file_path):
    try:
        # Read the content of the file
        with open(file_path, 'r') as file:
            code = file.read()

        # Query the LLM for refactoring suggestions
        output = query({
            "inputs": f"</s>[INST] Refactor the following code: {code} [/INST]",
        })

        if not output or not isinstance(output, list) or len(output) == 0:
            print(f"Invalid API response for file: {file_path}")
            return

        # Extract the refactored code from the response
        generated_text = output[0].get('generated_text', None)
        if not generated_text:
            print(f"No 'generated_text' in API response for file: {file_path}")
            return

        start_index = generated_text.find('[/INST]') + len('[/INST]')
        refactored_code = generated_text[start_index:].strip()

        # Write the refactored code back to the file
        with open(file_path, 'w') as file:
            file.write(refactored_code)

        print(f"Successfully refactored file: {file_path}")

    except Exception as e:
        print(f"Error refactoring file {file_path}: {e}")

def refactor_all_files():
    # Default file extensions to refactor (e.g., Python and Java files)
    file_extensions = ['.py', '.java']  # Add more extensions as needed

    # Get the current working directory (root of the repository)
    repo_path = os.getcwd()

    # Walk through the directory structure and refactor files
    for root, _, files in os.walk(repo_path):
        for file in files:
            # Skip the refactor.py file itself
            if file == "refactor.py":
                print(f"Skipping refactor.py file: {os.path.join(root, file)}")
                continue

            # Process only files with specified extensions
            if any(file.endswith(ext) for ext in file_extensions):
                file_path = os.path.join(root, file)
                print(f"Refactoring file: {file_path}")
                refactor_code(file_path)

if __name__ == "__main__":
    refactor_all_files()

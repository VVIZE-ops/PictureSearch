import requests
from flask import Flask, request,jsonify
from predict import getVertor
import os

app = Flask(__name__)


@app.route('/api/getvector', methods=['GET','POST'])
def getvector_v1():
    if request.method == 'POST':
        input_args = request.json
    else:
        input_args = request.args

    file_path = input_args['file_path']
    vector = getVertor(file_path).data
    return jsonify({'vector':vector.tolist(),'code':200})



if __name__ == '__main__':
    app.run(port=5000, debug=True)
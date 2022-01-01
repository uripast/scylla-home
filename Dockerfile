FROM python:3.9

COPY test.py .
CMD ["python3", "./test.py"]

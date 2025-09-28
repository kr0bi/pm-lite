FROM ubuntu:latest
LABEL authors="daniele"

ENTRYPOINT ["top", "-b"]
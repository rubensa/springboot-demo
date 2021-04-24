FROM rubensa/ubuntu-tini-dev-chrome-kdiff3

# Change docker socket permissions (Allow Docker In Docker)
RUN printf "\nsudo chmod a+rw /var/run/docker.sock\n" >> /home/${USER_NAME}/.bashrc

# Link SDKMan Java candidates path to standard Linux path
RUN sudo ln -s /opt/sdkman/candidates/java/ /usr/lib/jvm

# Create vscode extensions cache directory so it is owned by the user
RUN mkdir -p /home/${USER_NAME}/.vscode-server/extensionsCache

# Create maven repository directory so it is owned by the user
RUN mkdir -p /home/${USER_NAME}/.m2/repository


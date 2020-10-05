# Copyright 2020 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

FROM openjdk:8-jdk-alpine
ADD target/verneauth-*.jar /opt/verneauth/verneauth.jar
WORKDIR /opt/verneauth/

EXPOSE 8080

# use of "exec" see: https://stackoverflow.com/questions/31836498/sigterm-not-received-by-java-process-using-docker-stop-and-the-official-java-i
CMD exec java -jar verneauth.jar

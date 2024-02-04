#!/bin/bash
export SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3306/elyte?user=userExample&password=54321"
export JDBC_DRIVER="com.mysql.cj.jdbc.Driver"
export RABBIT_HOST="localhost"
export RABBITMQ_NODE_PORT=5672
export RABBITMQ_DEFAULT_USER="rabbitUser"
export RABBITMQ_DEFAULT_PASS="elyteRQ"
export LOST_QUEUE_NAME="LOST"
export LOST_ROUTING_KEY="lostItems"
export BOOKING_QUEUE_NAME="BOOKING"
export BOOKING_ROUTING_KEY="booking"
export SEARCH_QUEUE_NAME="SEARCH"
export SEARCH_ROUTING_KEY="search"
export EXCHANGE_NAME="elyteExchange"

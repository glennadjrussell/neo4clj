(ns neo4clj.graph
  ;; (:require [])
  (:import (org.neo4j.graphdb GraphDatabaseService DynamicLabel)
           (org.neo4j.graphdb.factory GraphDatabaseFactory)))

(def db-path "target/graphdb")
(def graph-factory (GraphDatabaseFactory.))

(defn connect-or-create
  "Connect to/create an embedded Neo4j database"
  []
  (.newEmbeddedDatabase graph-factory db-path))

(defn shutdown
  "Shutdown the graph "
  [g]
  (.shutdown g))

(defn- sane-key
  "Attempts to sanitise a map key from a keyword to a string as needed"
  [key]
  (if (keyword? key)
    (name key) 
    key))

(defn label-node
  "Labels a node n with the string l"
  [n l]
  (.addLabel n (DynamicLabel/label l)))

(defn node
  [g m]
  (let [n (.createNode g)]
    (doseq [[key val] m]
      (.setProperty n (sane-key key) val))
    n))

(defn tx
  "Given a graph, returns an open transaction"
  [g]
  (.beginTx g))

(defn success
  [t]
  (.success t))

(defmacro with-transaction [graph & body]
  "Taking a graph as an argument, starts a transaction then executes the body of statements, closing the transaction when done"
  `(let [tx# (.beginTx ~graph)]
     (let [ret# (do ~@body)]
       (.success tx#)
       ret#)))

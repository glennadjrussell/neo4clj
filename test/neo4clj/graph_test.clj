(ns neo4clj.graph-test
  (:require [clojure.test :refer :all]
            [neo4clj.graph :refer :all]))

(def gf (connect-or-create))

(deftest connect-test
  (testing "Creating an embedded database"
    (is (not (nil? gf)))))

(deftest create-node-test
  (testing "Creating a node with our database"
    (is (not (nil?
      (let [t (tx gf)]
        (node gf {})))))))

(deftest with-tx-test
  "Testing the scoping of a transaction"
  (is
    (not
      (nil?
        (with-transaction gf
          (node gf {})
          (node gf {:name "Test"})
          (node gf {"name" "Test"}))))))

(deftest label-node-test
  (testing "Labelling of a node"
    (is
        (nil?
          (with-transaction gf
            (label-node (node gf {:name "LabelTest"}) "TheActualLabel"))))))

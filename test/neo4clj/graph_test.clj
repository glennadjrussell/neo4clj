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
        (create-node gf {})))))))

(deftest create-relationship-test
  (testing "Creating a relationship"
    (let [r (create-relationship "is_friend_of")]
      (is (= (.name r) "is_friend_of"))
      (is (not (nil? r))))))

(deftest create-relationship-test-with-keyword
  (testing "Creating a relationship with a keyword"
    (let [r (create-relationship :is_enemy_of)]
      (println (.name r))
      (is (not (nil? r))))))

(deftest with-tx-test
  "Testing the scoping of a transaction"
  (is
    (not
      (nil?
        (with-transaction gf
          (create-node gf {})
          (create-node gf {:name "Test"})
          (create-node gf {"name" "Test"}))))))

(deftest with-tx-test-relationships
  "Testing the scoping of a transaction"
  (is
    (not
      (nil?
        (with-transaction gf
          (let [n1 (create-node gf {})
                n2 (create-node gf {:name "Test"})]
            (relate-to n1 n2 (create-relationship "TestRelationship"))))))))

(deftest label-node-test
  (testing "Labelling of a node"
    (is
        (nil?
          (with-transaction gf
            (label-node (create-node gf {:name "LabelTest"}) "TheActualLabel"))))))

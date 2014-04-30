/**
 *
 * Copyright 2013 lb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.gradle.plugin

import com.eriwen.gradle.js.util.FunctionalSpec

/**
 *
 */
class KarafFeaturesGenPluginTest extends FunctionalSpec {

    def "karaf feature file generation"() {
        given:
        buildFile <<
"""
apply plugin: 'java'

buildscript {
    dependencies {
        classpath files('../../../../libs/karaf-features-gen-1.0.0.SNAPSHOT.jar')
    }
}

repositories {
    mavenCentral()
}

apply plugin: 'karaf-featuresgen'

dependencies {
    compile 'org.scala-lang:scala-library:2.11.0'
}

karafFeatures {
    excludes = [
        'org.slf4j/.*',
        'log4j/.*',
        'org.osgi/.*',
        'org.apache.felix/.*',
        'org.apache.karaf.shell/.*'
    ]

    wraps = [
        'com.google.guava/guava/.*'
    ]

    startLevels = [
        'org.apache.geronimo.specs/.*':'50',
        'org.apache.commons/.*':'60',
    ]

    extraBundles = [
        'foo'
    ]

    outputFile = new File('$dir.dir.absolutePath/features.xml')
}
"""
		when:
        run "generateKarafFeatures"

		then:
        file("features.xml").text.indexOf('<bundle>mvn:org.scala-lang/scala-library/2.11.0</bundle>') > -1
        file("features.xml").text.indexOf('<bundle>foo</bundle>') > -1
        
        and:
        wasExecuted ":generateKarafFeatures"
    }
}

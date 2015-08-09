package org.pwd.application

import com.lyncode.jtwig.resource.ClasspathJtwigResource
import spock.lang.Specification

/**
 * @author bartosz.walacik
 */
class DummyTest extends Specification{

    def "should load jtwig resources"(){
        given:
        def res = new ClasspathJtwigResource("templates/home.html")
        println res

        expect:
        res.exists()
    }
}

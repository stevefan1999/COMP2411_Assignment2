/*
 * Copyright 2017 - 2019 EasyFXML project and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package hk.edu.polyu.comp2411.assignment.view

import hk.edu.polyu.comp2411.assignment.view.hello.HelloComponent
import javafx.stage.Stage
import moe.tristan.easyfxml.FxUiManager
import moe.tristan.easyfxml.api.FxmlComponent
import org.springframework.stereotype.Component

@Component
class HelloWorldUiManager(
    var helloComponent: HelloComponent
) : FxUiManager() {

    /**
     * @return the main [Stage]'s title you want
     */
    override fun title(): String {
        return "Hello, World!"
    }

    /**
     * @return the component to load as the root view in your main [Stage].
     */
    override fun mainComponent(): FxmlComponent {
        return helloComponent
    }

}
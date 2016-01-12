 /*
  * Copyright 2016 Aroma Tech.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package tech.sirwellington.alchemy.arguments.assertions;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.arguments.AlchemyAssertion;
import tech.sirwellington.alchemy.arguments.FailedAssertionException;

import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 *
 * @author SirWellington
 */
@NonInstantiable
public final class NetworkAssertions
{
    
    private final static Logger LOG = LoggerFactory.getLogger(NetworkAssertions.class);
    
    NetworkAssertions() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }
    
    /**
     * Checks that a URL is valid, according to the {@link URL} class.
     * 
     * @return 
     */
    public static AlchemyAssertion<String> validURL()
    {
        return string ->
        {
            nonEmptyString().check(string);
            
            try
            {
                URL url = new URL(string);
            }
            catch (Exception ex)
            {
                throw new FailedAssertionException("Invalid URL: " + string, ex);
            }
        };
    }
}

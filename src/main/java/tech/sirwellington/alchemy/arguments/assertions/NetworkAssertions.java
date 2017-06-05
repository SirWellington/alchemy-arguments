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
    
    /** The maximum allowable Port number */
    private static final int MAX_PORT = 65535;
    
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
        return new AlchemyAssertion<String>()
        {
            @Override
            public void check(String string) throws FailedAssertionException
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
            }
        };
    }
    
    
    /**
     * Asserts that a Port number is valid and acceptable.
     * 
     * @return 
     * @see <a href="https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers">https://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers</a>
     */
    public static AlchemyAssertion<Integer> validPort()
    {
        return new AlchemyAssertion<Integer>()
        {
            @Override
            public void check(Integer port) throws FailedAssertionException
            {
                if (port <= 0)
                {
                    throw new FailedAssertionException("Network port must be > 0");
                }

                if (port > MAX_PORT)
                {
                    throw new FailedAssertionException("Network port must <" + MAX_PORT);
                }
            }
        };
    }
}

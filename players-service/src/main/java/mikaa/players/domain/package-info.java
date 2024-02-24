/**
 * The domain module contains the core business models used throughout the
 * application. This idea stems from the clean architecture concept.
 * 
 * The main idea is to isolate the application layers from one another and use
 * the domain models as the common language between different parts of the
 * application. Notably, the service layer (a.k.a the "business logic") only
 * accepts the domain models as input, and similarly only exposes them as the
 * output of operations. The result is that the service layer may remain unaware
 * of the other layers of the application, such as the web layer (controllers)
 * and the messaging layer (Kafka producers and consumers). The other components
 * are responsible for accepting their inputs in the form of the common domain
 * objects, which they can convert to whatever form they need to.
 * 
 * @since 0.0.1
 * @author Mika Alaoutinen
 * @version 0.0.1
 */
package mikaa.players.domain;

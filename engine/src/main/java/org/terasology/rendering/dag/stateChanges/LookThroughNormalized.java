/*
 * Copyright 2016 MovingBlocks
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
package org.terasology.rendering.dag.stateChanges;

import com.google.common.base.Objects;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.dag.RenderPipelineTask;
import org.terasology.rendering.dag.StateChange;
import org.terasology.rendering.dag.tasks.LookThroughDefaultCameraTask;
import org.terasology.rendering.dag.tasks.LookThroughNormalizedTask;

// TODO: implement bobbing via multiple cameras and different steady/bobbing attachment points
/**
 * Instances of this class set the ModelView and Projection matrices
 * so that the scene can be rendered from a given camera.
 *
 * Differently from the LookThrough state change, a normalized camera does not bob up and down
 * when the player moves and bobbing is enabled.
 *
 * The default instance of this class resets both matrices to identity matrices, opengl's default.
 */
public class LookThroughNormalized implements StateChange {

    private static LookThroughNormalized defaultInstance = new LookThroughNormalized();

    private Camera camera;
    private RenderPipelineTask task;

    /**
     * Constructs an instance of this class initialised with the given camera.
     *
     * @param camera An instance implementing the Camera interface.
     */
    public LookThroughNormalized(Camera camera) {
        this.camera = camera;
    }

    private LookThroughNormalized() { }

    /**
     * Returns a task configured to set the modelview and projection matrixes so that the scene
     * is seen through a normalized version of the camera passed to the constructor.
     *
     * If the LookThroughNormalized instance is the default one, the task returned resets the matrices
     * to opengl's default (identity matrices).
     *
     * @return an instance implementing the RenderPipelineTask interface
     */
    @Override
    public RenderPipelineTask generateTask() {
        if (task == null) {
            if (camera != null) {
                task = new LookThroughNormalizedTask(camera);
            } else {
                task = new LookThroughDefaultCameraTask();
            }
        }
        return task;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(camera);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof LookThroughNormalized) && camera == ((LookThroughNormalized) obj).camera;
    }

    /**
     * Returns an instance of this class configured to generate a task resetting the ModelView and
     * Projection matrices back to opengl's default (identity matrices).
     *
     * @return the default instance of the LookThroughNormalized class
     */
    @Override
    public StateChange getDefaultInstance() {
        return defaultInstance;
    }

    @Override
    public boolean isTheDefaultInstance() {
        return this.equals(defaultInstance);
    }

    @Override
    public String toString() {
        if (this.isTheDefaultInstance()) {
            return String.format("%30s: %s", this.getClass().getSimpleName(), "default opengl camera");
        } else {
            return String.format("%30s: %s", this.getClass().getSimpleName(), camera.toString());
        }
    }
}
